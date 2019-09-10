function [new, ccs] = showStabilization(frames, templateIndex)
%SHOWSTABILIZATION Esegue e mostra statistiche per la stabilizzazione 
%                  di un video di frame rispetto a un template 
%                  scelto da un frame del video.
%
% La stabilizzazione avviene rispetto al centro del frame, ovvero
% l'immagine ricavata dalla correlazione di ogni frame del video e del
% template viene allineata al centro del video.
%
%  INPUT
%   frames: Video di frame da stabilizzare;
%   templateIndex: Indice del frame dal quale prendere il template per fare
%                  la stabilizzazione;
%  OUTPUT
%   new: Risultato della stabilizzazione;
%   ccs: Immagine della Cross Correlazione 2D del video di frame con
%        template.
% 
   
[R,C,~,~] = size(frames);

% Ricavo il template ritagliando il frame sul video a indice templateIndex
figure; template = imcrop(frames(:,:,:,templateIndex));
template = rgb2gray(template);
[tR,tC] = size(template);

% Scorro ogni frame del video
figure('Renderer', 'painters', 'Position', [300 200 900 600]);
for i=1:size(frames,4)
    % Eseguo la cross correlazione 2D su ogni frame usando template
    comp  = frames(:,:,:,i);
    compg = rgb2gray(comp);
    cc = normxcorr2(template,compg);
    
    % Calcolo il punto di massima correlazione tra le due immagini e le
    % coordinate di questo punto
    [max_cc, imax] = max((cc(:)));
    [ypeak, xpeak] = ind2sub(size(cc),imax);
    
    % Calcolo l'offset del template sul singolo frame
    corr_offset = [(ypeak-size(template,1)) (xpeak-size(template,2))];
    
    % Eseguo la traslazione del frame riportando al centro l'immagine 
    % ricavata dalla cross correlazione:
    %  1) Tolgo l'offset appena ricavato così da allineare il vertice in 
    %     alto a sinistra del template trovato dalla cross correlazione
    %     con il frame, con il vertice in alto a sinistra del frame;
    %  2) Tolgo la metà delle dimensioni del template, così da allineare
    %     il vertice in alto a sinistra del frame con il centro del 
    %     template trovato dalla cross correlazione;
    %  3) Aggiungo la metà delle dimensioni del frame così da allineare il
    %     centro del template trovato dalla cross correlazione con il
    %     centro del frame.
    %     
    %new(:,:,:,i) = imtranslate(frames(:,:,:,i),[-(corr_offset(2)+round(C/2)), -(corr_offset(1))],'FillValues',0);
    new(:,:,:,i) = imtranslate(frames(:,:,:,i),[-(corr_offset(2) + round(tC/2)) + round(C/2), -(corr_offset(1) + round(tR/2)) + round(R/2)],'FillValues',0);
    ccs(:,:,i) = cc;

    % Mostro il template scelto, l'immagine originale, il grafico di cross
    % correlazione e l'immagine stabilizzata.
    subplot(221); imshow(template); axis image; title(strcat('Template scelto n°', num2str(templateIndex)));
    subplot(222); imshow(frames(:,:,:,i)); axis image;  title(strcat('Immagine originale: ', num2str(i)));
    subplot(223); imagesc(ccs(:,:,i)); colorbar; title({'Mappa di cross-correlazione 2D', strcat('MaxCCValue:', num2str(max_cc))});
    hold on;      scatter(xpeak, ypeak,'rX');
    subplot(224); imshow(new(:,:,:,i)); title('Immagine stabilizzata');

    pause(0.1)
end

end